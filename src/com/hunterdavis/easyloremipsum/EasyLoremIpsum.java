package com.hunterdavis.easyloremipsum;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class EasyLoremIpsum extends Activity {

	String ourSaveFileName = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// set up the units spinner
		Spinner spinner = (Spinner) findViewById(R.id.unitspin);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.units, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyUnitsOnItemSelectedListener());

		// set up numerals spinner\
		spinner = (Spinner) findViewById(R.id.numeralspin);
		adapter = ArrayAdapter.createFromResource(this, R.array.numerals,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new MyUnitsOnItemSelectedListener());
		
		
		// Create an anonymous implementation of OnClickListener
		OnClickListener saveButtonListner = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				Boolean didWeSave = saveFile(v.getContext());
		}
		};
		
		
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(saveButtonListner);

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

	}

	// set up the listener class for spinner
	class MyUnitsOnItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			fillText(view.getContext());
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	public void fillText(Context context) {
		// do something
		EditText fullText = (EditText) findViewById(R.id.hiddentext);
		Spinner unitspinner = (Spinner) findViewById(R.id.unitspin);
		int unitsPos = unitspinner.getSelectedItemPosition();
		Spinner numberspinner = (Spinner) findViewById(R.id.numeralspin);
		int number = numberspinner.getSelectedItemPosition() + 1;
		String ourFullText = "";
		String unitsText = "";
		for (int i = 0; i < number; i++) {
			switch (unitsPos) {
			case 0:
				ourFullText += generateSentence();
				unitsText = "sentences";
				break;
			case 1:
				ourFullText += generateParagraph();
				unitsText = "paragraphs";
				break;

			case 2:
				ourFullText += generatePage();
				unitsText = "pages";
				break;

			case 3:
				ourFullText += generateChapter();
				unitsText = "chapters";
				break;
			default:
				Toast.makeText(context, "We should never get here",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

		fullText.setText(ourFullText);
		ourSaveFileName = "Easy-Lorem-Ipsum-" + number + "-" + unitsText
				+ ".txt";

	}

	public String generateSentence() {
		final Random myRandom = new Random();
		Resources res = getResources();
		String[] loremipsemarray = res.getStringArray(R.array.sentences);
		int arraylength = loremipsemarray.length;
		int myNewRandomPosition = myRandom.nextInt(arraylength);
		String retString = loremipsemarray[myNewRandomPosition] + "  ";
		return retString;
	}

	// a paragraph is 4-6 sentences
	public String generateParagraph() {
		final Random myRandom = new Random();
		int myNewRandomPosition = myRandom.nextInt(2) + 4;
		String retString = "";
		for (int i = 0; i < myNewRandomPosition; i++) {
			retString += generateSentence();
		}
		retString += "\n\n";
		return retString;

	}

	// a page is 3-4 paragraphs
	public String generatePage() {
		final Random myRandom = new Random();
		int myNewRandomPosition = myRandom.nextInt(1) + 3;
		String retString = "";
		for (int i = 0; i < myNewRandomPosition; i++) {
			retString += generateParagraph();
		}
		retString += "\f";
		return retString;
	}

	// a chapter is 10 pages
	public String generateChapter() {
		String retString = "";
		for (int i = 0; i < 10; i++) {
			retString += generatePage();
		}
		return retString;
	}

	public Boolean saveFile(Context context) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		if (ourSaveFileName.length() < 3) {
			return false;
		}
		// actually save the file
		EditText fullText = (EditText) findViewById(R.id.hiddentext);
		String text = fullText.getText().toString();

		if (text.length() < 3) {
			return false;
		}

		File logFile = new File(extStorageDirectory, ourSaveFileName);

		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.write(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(context, "Saved " + ourSaveFileName, Toast.LENGTH_LONG)
				.show();

		return false;
	}

}