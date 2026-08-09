package com.homegui.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Locale;

/**
 * Turns the colour markup players type into styled text.
 *
 * <p>Both {@code §} and {@code &} introduce a code. The one letter codes are the vanilla ones
 * ({@code 0} to {@code f} for colours, {@code k} to {@code o} for formats, {@code r} to reset),
 * and {@code &#RRGGBB} gives any RGB colour. Hex has to become a real {@link Style}, because
 * the {@code §x} trick some server platforms use means nothing to a vanilla client.
 */
public final class ColorCodes {
	public static final char SECTION = '§';
	public static final char AMPERSAND = '&';

	private static final char HEX_PREFIX = '#';
	private static final int HEX_DIGITS = 6;
	private static final int HEX_RADIX = 16;

	private ColorCodes() {
	}

	// ---------------------------------------------------------------- parse

	/** Builds styled text from markup. Text without any markup simply comes back as is. */
	public static MutableComponent parse(String raw) {
		return parse(raw, Style.EMPTY);
	}

	/**
	 * Builds styled text that starts from an existing style, so a colour opened earlier in a
	 * message keeps applying to this piece.
	 */
	public static MutableComponent parse(String raw, Style start) {
		MutableComponent result = Component.empty();

		if (raw == null || raw.isEmpty()) {
			return result;
		}

		StringBuilder pending = new StringBuilder();
		Style style = start;
		int index = 0;

		while (index < raw.length()) {
			int consumed = codeLengthAt(raw, index);

			if (consumed == 0) {
				pending.append(raw.charAt(index));
				index++;
				continue;
			}

			flush(result, pending, style);
			style = applyCode(style, raw, index);
			index += consumed;
		}

		flush(result, pending, style);
		return result;
	}

	/** The text without any markup, which is what length checks and lookups work on. */
	public static String strip(String raw) {
		if (raw == null || raw.isEmpty()) {
			return "";
		}

		StringBuilder plain = new StringBuilder(raw.length());
		int index = 0;

		while (index < raw.length()) {
			int consumed = codeLengthAt(raw, index);

			if (consumed == 0) {
				plain.append(raw.charAt(index));
				index++;
			} else {
				index += consumed;
			}
		}

		return plain.toString();
	}

	/**
	 * The style left in effect after the given text, used to carry formatting across the
	 * pieces a message is assembled from.
	 */
	public static Style styleAfter(Style start, String text) {
		Style style = start;
		int index = 0;

		while (index < text.length()) {
			int consumed = codeLengthAt(text, index);

			if (consumed == 0) {
				index++;
			} else {
				style = applyCode(style, text, index);
				index += consumed;
			}
		}

		return style;
	}

	// --------------------------------------------------------------- internals

	/** How many characters the code at this position takes, or 0 when there is no code. */
	private static int codeLengthAt(String text, int index) {
		char marker = text.charAt(index);

		if (marker != SECTION && marker != AMPERSAND || index + 1 >= text.length()) {
			return 0;
		}

		char next = text.charAt(index + 1);

		if (next == HEX_PREFIX) {
			return isHex(text, index + 2) ? 2 + HEX_DIGITS : 0;
		}

		return isLegacyCode(next) ? 2 : 0;
	}

	private static Style applyCode(Style style, String text, int index) {
		char next = text.charAt(index + 1);

		if (next == HEX_PREFIX) {
			int rgb = Integer.parseInt(text.substring(index + 2, index + 2 + HEX_DIGITS), HEX_RADIX);
			return style.withColor(TextColor.fromRgb(rgb));
		}

		char code = Character.toLowerCase(next);

		if (code == 'r') {
			return Style.EMPTY;
		}

		ChatFormatting formatting = ChatFormatting.getByCode(code);

		if (formatting == null) {
			return style;
		}

		// A colour code clears the active formats, the same way the vanilla renderer does.
		return isColour(code) ? Style.EMPTY.withColor(formatting) : style.applyFormat(formatting);
	}

	private static boolean isLegacyCode(char raw) {
		char code = Character.toLowerCase(raw);
		return isColour(code) || (code >= 'k' && code <= 'o') || code == 'r';
	}

	private static boolean isColour(char code) {
		return (code >= '0' && code <= '9') || (code >= 'a' && code <= 'f');
	}

	private static boolean isHex(String text, int start) {
		if (start + HEX_DIGITS > text.length()) {
			return false;
		}

		for (int offset = 0; offset < HEX_DIGITS; offset++) {
			if (Character.digit(text.charAt(start + offset), HEX_RADIX) < 0) {
				return false;
			}
		}

		return true;
	}

	private static void flush(MutableComponent target, StringBuilder pending, Style style) {
		if (pending.isEmpty()) {
			return;
		}

		target.append(Component.literal(pending.toString()).withStyle(style));
		pending.setLength(0);
	}

	/** Lower cased plain text, the form used as a map key so lookups ignore colours and case. */
	public static String key(String raw) {
		return strip(raw).toLowerCase(Locale.ROOT);
	}
}
