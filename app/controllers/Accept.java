/* Copyright 2014-2016, hbz. Licensed under the Eclipse Public License 1.0 */

package controllers;

import java.util.Collection;

import play.api.http.MediaRange;

/**
 * Helper class for dealing with content negotiation for the `Accept` header.
 * 
 * @author Fabian Steeg (fsteeg)
 *
 */
public class Accept {

	private Accept() {
		// static helper class, don't instantiate
	}

	enum Format {
		JSON_LD("json", "application/json", "application/ld+json"), //
		HTML("html", "text/html"), //
		JAVASCRIPT("js", "text/javascript", "application/javascript"), //
		CSV("csv", "text/csv");

		String[] types;
		String queryParamString;

		private Format(String format, String... types) {
			this.queryParamString = format;
			this.types = types;
		}
	}

	static String formatFor(String formatParam,
			Collection<MediaRange> acceptedTypes) {
		for (Format format : Format.values())
			if (formatParam != null && format.queryParamString
					.equals(formatParam.split(Application.FORMAT_CONFIG_SEP)[0]))
				return formatParam;
		for (MediaRange mediaRange : acceptedTypes)
			for (Format format : Format.values())
				for (String mimeType : format.types)
					if (mediaRange.accepts(mimeType))
						return format.queryParamString;
		return Format.JSON_LD.queryParamString;
	}

}
