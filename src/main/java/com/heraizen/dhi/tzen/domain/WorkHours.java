package com.heraizen.dhi.tzen.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkHours {
	
	private int mon;
	private int tue;
	private int wed;
	private int thu;
	private int fri;
	private int sat;
	private int sun;
	public int getMon() {
		return mon;
	}
	public void setMon(int mon) {
		this.mon = mon;
	}
	public int getTue() {
		return tue;
	}
	public void setTue(int tue) {
		this.tue = tue;
	}
	public int getWed() {
		return wed;
	}
	public void setWed(int wed) {
		this.wed = wed;
	}
	public int getThu() {
		return thu;
	}
	public void setThu(int thu) {
		this.thu = thu;
	}
	public int getFri() {
		return fri;
	}
	public void setFri(int fri) {
		this.fri = fri;
	}
	public int getSat() {
		return sat;
	}
	public void setSat(int sat) {
		this.sat = sat;
	}
	public int getSun() {
		return sun;
	}
	public void setSun(int sun) {
		this.sun = sun;
	}
	
	

}
