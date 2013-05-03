package com.xiantrimble.combinatorics.cli;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;

public class CombinatoricsCli {
  public static void main( String[] argv ) {
    Runnable command = parse(argv, new CommandGenerate(), new CommandCount());
    command.run();
  }
  
  public static Runnable parse( String[] argv, Runnable... commands ) {
    
    final JCommander jc = new JCommander();
    for( Runnable command : commands ) {
      jc.addCommand(command);
    }
    
    jc.parse(argv);
    
    String parsedCommand = jc.getParsedCommand();
    for( Runnable command : commands ) {
      if( ArrayUtils.contains(command.getClass().getAnnotation(Parameters.class).commandNames(), parsedCommand) ) {
        return command;
      }
    }
    
    return new Runnable() {
      @Override
      public void run() {
        jc.usage();
      }
    };
  }
}
