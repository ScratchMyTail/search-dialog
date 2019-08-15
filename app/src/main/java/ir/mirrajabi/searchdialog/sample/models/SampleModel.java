package ir.mirrajabi.searchdialog.sample.models;

import ir.mirrajabi.searchdialog.core.Searchable;


/**
 * Created by MADNESS on 5/16/2017.
 */

public class SampleModel implements Searchable {
	private String mTitle;
	
	public SampleModel(String title) {
		mTitle = title;
	}
	
	@Override
	public String getTitle() {
		return mTitle;
	}

	@Override
	public String getSubtitle() {
		return "Subtitle";
	}

	public SampleModel setTitle(String title) {
		mTitle = title;
		return this;
	}
}
