package com.anna.sent.soft.childbirthdate.age;

import java.util.ArrayList;
import java.util.List;

public class SettingsParser {
	public static String DELIMITER_LIST = ";";

	public static String saveList(List<ISetting> list) {
		String result = "";
		for (int i = 0; i < list.size(); ++i) {
			result += list.get(i).save()
					+ (i == list.size() - 1 ? "" : DELIMITER_LIST);
		}

		return result;
	}

	public static List<ISetting> loadList(String str, ISetting element) {
		List<ISetting> result = new ArrayList<ISetting>();

		if (str != null) {
			String[] tokens = str.split(DELIMITER_LIST);
			for (int i = 0; i < tokens.length; ++i) {
				ISetting obj = element.load(tokens[i]);
				if (obj != null) {
					result.add(obj);
				}
			}
		}

		return result;
	}

	public static List<LocalizableObject> toList(String str, ISetting element) {
		List<ISetting> source = SettingsParser.loadList(str, element);
		List<LocalizableObject> destination = new ArrayList<LocalizableObject>();

		for (int i = 0; i < source.size(); ++i) {
			destination.add(source.get(i));
		}

		return destination;
	}

	public static String toString(List<LocalizableObject> list, ISetting element) {
		List<LocalizableObject> source = list;
		List<ISetting> destination = new ArrayList<ISetting>();

		for (int i = 0; i < source.size(); ++i) {
			destination.add((ISetting) source.get(i));
		}

		return SettingsParser.saveList(destination);
	}
}