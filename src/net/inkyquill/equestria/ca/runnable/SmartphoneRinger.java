// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RealisticChat.java

package net.inkyquill.equestria.ca.runnable;

import java.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

// Referenced classes of package me.exphc.RealisticChat:
//            RealisticChat

class SmartphoneRinger
    implements Runnable
{

    public SmartphoneRinger(RealisticChat realisticchat, Player player1)
    {
        plugin = realisticchat;
        player = player1;
        noteblockLocation = player1.getLocation().add(0.0D, 2D, 0.0D);
        player1.sendBlockChange(noteblockLocation, Material.NOTE_BLOCK, (byte)0);
        ArrayList arraylist = new ArrayList();
        arraylist.add(new Note(1, org.bukkit.Note.Tone.A, false));
        arraylist.add(new Note(1, org.bukkit.Note.Tone.A, true));
        arraylist.add(new Note(1, org.bukkit.Note.Tone.A, false));
        arraylist.add(new Note(1, org.bukkit.Note.Tone.A, false));
        notesIterator = arraylist.iterator();
        long l = 0L;
        long l1 = 10L;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(realisticchat, this, l, l1);
    }

    public void run()
    {
        if(notesIterator.hasNext())
        {
            Note note = (Note)notesIterator.next();
            player.playNote(noteblockLocation, Instrument.PIANO, note);
        } else
        {
            Bukkit.getScheduler().cancelTask(taskId);
            Block block = noteblockLocation.getBlock();
            player.sendBlockChange(noteblockLocation, block.getType(), block.getData());
        }
    }

    private final Location noteblockLocation;
    private final Iterator notesIterator;
    private final int taskId;
    private final Player player;
    private final RealisticChat plugin;
}
