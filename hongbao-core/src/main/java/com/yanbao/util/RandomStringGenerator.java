package com.yanbao.util;

import java.security.SecureRandom;

public final class RandomStringGenerator {

	private static final char[] PRINTABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345679".toCharArray();

	private static final int DEFAULT_MAX_RANDOM_LENGTH = 8;

	private SecureRandom randomizer = new SecureRandom();

	private final int maximumRandomLength;

	public RandomStringGenerator() {
		this(DEFAULT_MAX_RANDOM_LENGTH);
	}

	public RandomStringGenerator(final int maxRandomLength) {
		this.maximumRandomLength = maxRandomLength;
	}

	public String getNewString() {
		final byte[] random = getNewStringAsBytes();
		return convertBytesToString(random);
	}

	public byte[] getNewStringAsBytes() {
		final byte[] random = new byte[this.maximumRandomLength];
		this.randomizer.nextBytes(random);
		return random;
	}

	private String convertBytesToString(final byte[] random) {
		final char[] output = new char[random.length];
		for (int i = 0; i < random.length; i++) {
			final int index = Math.abs(random[i] % PRINTABLE_CHARACTERS.length);
			output[i] = PRINTABLE_CHARACTERS[index];
		}
		return new String(output);
	}

	public int getMaxLength() {
		return this.maximumRandomLength;
	}
}