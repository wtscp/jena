/*
 * (c) Copyright 2011 Epimorphics Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.tdb.transaction ;

import java.util.Map ;

import com.hp.hpl.jena.tdb.DatasetGraphTxn ;
import com.hp.hpl.jena.tdb.ReadWrite ;
import com.hp.hpl.jena.tdb.TDBException ;
import com.hp.hpl.jena.tdb.base.block.BlockMgr ;
import com.hp.hpl.jena.tdb.base.block.BlockMgrLogger ;
import com.hp.hpl.jena.tdb.base.block.BlockMgrReadonly ;
import com.hp.hpl.jena.tdb.base.file.BufferChannel ;
import com.hp.hpl.jena.tdb.base.file.BufferChannelFile ;
import com.hp.hpl.jena.tdb.base.file.BufferChannelMem ;
import com.hp.hpl.jena.tdb.base.file.FileFactory ;
import com.hp.hpl.jena.tdb.base.file.FileSet ;
import com.hp.hpl.jena.tdb.base.objectfile.ObjectFile ;
import com.hp.hpl.jena.tdb.base.record.RecordFactory ;
import com.hp.hpl.jena.tdb.index.Index ;
import com.hp.hpl.jena.tdb.index.IndexMap ;
import com.hp.hpl.jena.tdb.nodetable.NodeTable ;
import com.hp.hpl.jena.tdb.nodetable.NodeTableInline ;
import com.hp.hpl.jena.tdb.nodetable.NodeTableReadonly ;
import com.hp.hpl.jena.tdb.setup.BlockMgrBuilder ;
import com.hp.hpl.jena.tdb.setup.DatasetBuilderStd ;
import com.hp.hpl.jena.tdb.setup.NodeTableBuilder ;
import com.hp.hpl.jena.tdb.store.DatasetGraphTDB ;
import com.hp.hpl.jena.tdb.sys.FileRef ;
import com.hp.hpl.jena.tdb.sys.Names ;
import com.hp.hpl.jena.tdb.sys.SystemTDB ;

public class DatasetBuilderTxn
{
    private TransactionManager txnMgr ;
    Map<FileRef, BlockMgr> blockMgrs ; 
    Map<FileRef, NodeTable> nodeTables ;
    Journal journal ;
    Transaction txn ;

    public DatasetBuilderTxn(TransactionManager txnMgr) { this.txnMgr = txnMgr ; }
    
    public DatasetGraphTDB build(Transaction transaction, ReadWrite mode, DatasetGraphTDB dsg)
    {
        this.blockMgrs = dsg.getConfig().blockMgrs ;
        this.nodeTables = dsg.getConfig().nodeTables ;
        this.txn = transaction ;
            
        switch(mode)
        {
            case READ : return buildReadonly(transaction, dsg) ;
            case WRITE : return buildWritable(transaction, dsg) ;
            default: return null ;  // Silly Java.
        }
    }
    
    private DatasetGraphTxn buildReadonly(Transaction transaction, DatasetGraphTDB dsg)
    {
        BlockMgrBuilder blockMgrBuilder = new BlockMgrBuilderReadonly() ;
        NodeTableBuilder nodeTableBuilder = new NodeTableBuilderReadonly() ;
        DatasetBuilderStd x = new DatasetBuilderStd(blockMgrBuilder, nodeTableBuilder) ;
        DatasetGraphTDB dsg2 = x.build(dsg.getLocation(), dsg.getConfig().properties) ;
        return new DatasetGraphTxn(dsg2, txn) ;
    }

    private DatasetGraphTDB buildWritable(Transaction transaction, DatasetGraphTDB dsg)
    {
        BufferChannel chan ;
        if ( dsg.getLocation().isMem() )
            chan = BufferChannelMem.create() ;
        else
            chan = new BufferChannelFile(dsg.getLocation().absolute(Names.journalFile)) ;
        journal = new Journal(chan) ;
        txn.setJournal(journal) ;
        
        BlockMgrBuilder blockMgrBuilder = new BlockMgrBuilderTx() ;
        NodeTableBuilder nodeTableBuilder = new NodeTableBuilderTx() ;
        DatasetBuilderStd x = new DatasetBuilderStd(blockMgrBuilder, nodeTableBuilder) ;
        DatasetGraphTDB dsg2 = x.build(dsg.getLocation(), dsg.getConfig().properties) ;
        return new DatasetGraphTxn(dsg2, txn) ;
    }

    // ---- Add logging to a BlockMgr when built.
    static BlockMgrBuilder logging(BlockMgrBuilder other) { return new BlockMgrBuilderLogger(other) ; }
    
    static class BlockMgrBuilderLogger implements BlockMgrBuilder
    {
        public BlockMgrBuilder other ;
        public BlockMgrBuilderLogger(BlockMgrBuilder other)
        { 
            this.other = other ;
        }
        
        @Override
        public BlockMgr buildBlockMgr(FileSet fileSet, String ext, int blockSize)
        {
            BlockMgr blkMgr = other.buildBlockMgr(fileSet, ext, blockSize) ;
            blkMgr = new BlockMgrLogger(blkMgr.getLabel(), blkMgr, true) ;
            return blkMgr ;
        }
    }
    
    // ---- Build transactional versions for update.
    
    class NodeTableBuilderTx implements NodeTableBuilder
    {
        @Override
        public NodeTable buildNodeTable(FileSet fsIndex, FileSet fsObjectFile, int sizeNode2NodeIdCache,
                                        int sizeNodeId2NodeCache)
        {
            FileRef ref = FileRef.create(fsObjectFile.filename(Names.extNodeData)) ;
            NodeTable ntBase = nodeTables.get(ref) ;
            if ( ntBase == null )
                throw new TDBException("No NodeTable for "+ref) ;
            
            RecordFactory recordFactory = new RecordFactory(SystemTDB.LenNodeHash, SystemTDB.SizeOfNodeId) ;
            Index idx = new IndexMap(recordFactory) ;
            String objFilename = fsObjectFile.filename(Names.extNodeData+"-"+Names.extJournal) ;
            ObjectFile objectFile ;
            if ( fsObjectFile.isMem() )
                objectFile = FileFactory.createObjectFileMem() ;
            else
                objectFile = FileFactory.createObjectFileDisk(objFilename) ;

            NodeTableTrans ntt = new NodeTableTrans(ntBase, idx, objectFile) ;
            txn.add(ntt) ;
            
            // Add inline wrapper.
            NodeTable nt = NodeTableInline.create(ntt) ;
            return nt ;
        }
    }
    
    class BlockMgrBuilderTx implements BlockMgrBuilder
    {
        @Override
        public BlockMgr buildBlockMgr(FileSet fileSet, String ext, int blockSize)
        {
            // Find from file ref.
            FileRef ref = FileRef.create(fileSet, ext) ;
            BlockMgr baseMgr = blockMgrs.get(ref) ;
            if ( baseMgr == null )
                throw new TDBException("No BlockMgr for "+ref) ;
            BlockMgrJournal blkMgr = new BlockMgrJournal(txn, ref, baseMgr, journal) ;
            txn.add(blkMgr) ;
            return blkMgr ;
        }
    }

    // ---- Build passthrough versions for readonly access
    
    class BlockMgrBuilderReadonly implements BlockMgrBuilder
    {
        @Override
        public BlockMgr buildBlockMgr(FileSet fileSet, String ext, int blockSize)
        {
            FileRef ref = FileRef.create(fileSet, ext) ;
            BlockMgr blockMgr = blockMgrs.get(ref) ;
            if ( blockMgr == null )
                throw new TDBException("No BlockMgr for "+ref) ;
            blockMgr = new BlockMgrReadonly(blockMgr) ;
            return blockMgr ;
        }
    }
    
    class NodeTableBuilderReadonly implements NodeTableBuilder
    {

        @Override
        public NodeTable buildNodeTable(FileSet fsIndex, FileSet fsObjectFile, int sizeNode2NodeIdCache,
                                        int sizeNodeId2NodeCache)
        {
            FileRef ref = FileRef.create(fsObjectFile.filename(Names.extNodeData)) ;
            NodeTable nt = nodeTables.get(ref) ;
            nt = new NodeTableReadonly(nt) ;
            return nt ;
        }
    }
}

/*
 * (c) Copyright 2011 Epimorphics Ltd. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. The name of the author may not
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */