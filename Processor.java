import java.util.*;

import javax.sound.midi.*;

public class Processor {
	
	private Storage dataInside;
	public final static int NUMBER = 0;
	public final static int OPERATION_SYMBOL = 1;
	public final static int STACK_OPERATION = 2;
	public final static int MIDI_OPERATION = 3;
	public final static int ERROR_TYPE = 99; //Error type will be dealt later.
	
	public Processor() {
		dataInside = new Storage();
	}
	
	/**
	 * This will return the height of stack
	 * @return
	 */
	protected int getStackHeight() {
		return dataInside.size();
	}
	
	protected void printStorage() {
		System.out.print(dataInside);
	}
	

	/**
	 * In this method, all input will be in the format of String, afterwards if it returns NUMBER, input will be parsed into int and stored into Storage.
	 * @param input
	 * @return Type of input
	 */
	protected int inputTypeDiagnosis(String input) {
		
		String[] operation_symbols = {"+", "-", "*", "/"};
		String[] stack_operation = {"pop", "clear", "quit"};
		String midi_operation = "midi";
		
		if (isIn(input, operation_symbols)) {
			//System.out.println("This is a number operation");	
			return OPERATION_SYMBOL;
		}else if (isIn(input, stack_operation)) {		
			//System.out.println("This is a stack operation");			
			return STACK_OPERATION;
		}else if (input.equals(midi_operation)) {
			//System.out.println("This is a midi operation");
			return MIDI_OPERATION;
		}else {			
			//System.out.println("This is a number");
			return NUMBER;
		}
	}
	
	
	/**
	 * check if target is in list
	 * @param target
	 * @param list
	 * @return boolean
	 */
	private boolean isIn(String target, String[] list) {
		for(int i =0; i < list.length; i++) {
			if (target.equals(list[i])) { //use .equals() to test if two string are the same in value.
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This checks whether midi is available.
	 * @return boolean
	 */
	private boolean enough_data_to_do_midi() throws InsufficientMidiInfoException {
		if (dataInside.size() < 3) {
			throw new InsufficientMidiInfoException("Error! The stack must have at least 3 numbers for midi to play...");
		}else {
			return true;
		}
	}
	
	private boolean isValidData(int note) throws InvalidMidiDataException {
		if (note <= 127 && note >= 0) {
			return true;
		}else {
			throw new InvalidMidiDataException("Error! The number"+note+"is outside of the valid MIDI data range...");
		}
	}
	
	protected void playMidi(int duration, int note, int instrument) {
		try {
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			ShortMessage a = new ShortMessage();
			a.setMessage(144, 1, instrument, 100);
			MidiEvent noteOn = new MidiEvent(a, 1);
			track.add(noteOn);
			
			ShortMessage b = new ShortMessage();
			b.setMessage(144, 1, note, 100);
			MidiEvent noteOff = new MidiEvent(b, duration);
			track.add(noteOff);
			
			player.setSequence(seq);
			player.start();
			
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * after checking can_do_midi(), call playMidi() to play midi sound
	 */
	protected void process_midi() {
		try {
			boolean enough_data = enough_data_to_do_midi();
			if (enough_data) {
				int first_out = dataInside.get();
				int second_out = dataInside.get();
				int third_out = dataInside.get();
				boolean all_data_valid = isValidData(second_out);
				//get the top three numbers from the stack, and then use them to play midi
				if (all_data_valid) {
					playMidi(third_out, second_out, first_out);
					System.out.println("Playing MIDI sound: Duration="+third_out+", Note="+second_out+", Instrument="+first_out+"...");
				}
				Integer[] repushArray = new Integer[3];
				repushArray[0] = new Integer(third_out);
				repushArray[1] = new Integer(second_out);
				repushArray[2] = new Integer(first_out);
				for (int i=0; i<3; i++) {
					dataInside.set(repushArray[i]);
				}
				//after taking that three numbers out, repush them back
			}
		}catch (InsufficientMidiInfoException e) {
			System.out.println(e);
			e.printStackTrace();
		}catch (InvalidMidiDataException e2) {
			System.out.println(e2);
			e2.printStackTrace();
		}
	}
	
	
	/**
	 * first check the type of input by calling inputTypeDiagnosis(), if input is a NUMBER, then store it into dataInside as an Integer. If not a NUMBER,
	 * do corresponding operation based on the type of input.
	 * @param input
	 */
	protected void process(String input) {
		int inputType = inputTypeDiagnosis(input);
		if (inputType == NUMBER) {
			int inputNumber = Integer.parseInt(input);
			Integer inputNumber_as_Integer = new Integer(inputNumber);
			dataInside.set(inputNumber_as_Integer);
			//store input number into Storage
		}else if (inputType == OPERATION_SYMBOL) {
			int last_int = dataInside.get();
			int second_last = dataInside.get();
			int result = 0;
			//get the first two numbers on the stack and then do calculation. In the end push the result to the stack
			if (input.equals("+")) {
				result = second_last + last_int;
			}else if (input.equals("-")) {
				//System.out.println("I'm here");
				result = second_last - last_int;
			}else if (input.equals("*")) {
				result = second_last * last_int;
			}else if (input.equals("/")) {
				result = second_last / last_int;
			}
			System.out.println(second_last+input+last_int+"="+result);
			Integer result_as_integer = new Integer(result);
			dataInside.set(result_as_integer);
		}else if (inputType == STACK_OPERATION) {
			if (input.equals("clear")) {
				dataInside = new Storage();
				//replace the old storage with a new one
			}else if (input.equals("pop")) {
				dataInside.get();
			}else if (input.equals("quit")) {
				return;
				//end this application
			}
		}else if (inputType == MIDI_OPERATION) {
			process_midi();
		}
	}
}
