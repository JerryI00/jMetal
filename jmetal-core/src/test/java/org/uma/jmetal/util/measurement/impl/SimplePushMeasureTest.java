package org.uma.jmetal.util.measurement.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.uma.jmetal.util.measurement.MeasureListener;

public class SimplePushMeasureTest {

	@Test
	public void testClassNameWhenNoName() {
		assertEquals(SimplePushMeasure.class.getSimpleName(),
				new SimplePushMeasure<Integer>().getName());
	}

	@Test
	public void testNullDescriptionWhenNoDescription() {
		assertNull(new SimplePushMeasure<Integer>().getDescription());
		assertNull(new SimplePushMeasure<Integer>("name").getDescription());
	}

	@Test
	public void testCorrectNameWhenProvided() {
		String name = "named measure";
		assertEquals(name, new SimplePushMeasure<Integer>(name).getName());
		assertEquals(name,
				new SimplePushMeasure<Integer>(name, "description").getName());
	}

	@Test
	public void testCorrectDescriptionWhenProvided() {
		String description = "My measure description is awesome!";
		assertEquals(description, new SimplePushMeasure<Integer>("measure",
				description).getDescription());
	}

	@Test
	public void testNotifiedWhenRegistered() {
		final Integer[] lastReceived = { null };
		MeasureListener<Integer> listener = new MeasureListener<Integer>() {

			@Override
			public void measureGenerated(Integer value) {
				lastReceived[0] = value;
			}
		};
		SimplePushMeasure<Integer> pusher = new SimplePushMeasure<Integer>();
		pusher.register(listener);

		pusher.push(3);
		assertEquals(3, (Object) lastReceived[0]);
		pusher.push(null);
		assertEquals(null, (Object) lastReceived[0]);
		pusher.push(5);
		assertEquals(5, (Object) lastReceived[0]);
	}

	@Test
	public void testNotNotifiedWhenUnregistered() {
		final Integer[] lastReceived = { null };
		MeasureListener<Integer> listener = new MeasureListener<Integer>() {

			@Override
			public void measureGenerated(Integer value) {
				lastReceived[0] = value;
			}
		};
		SimplePushMeasure<Integer> pusher = new SimplePushMeasure<Integer>();
		pusher.register(listener);
		pusher.unregister(listener);

		pusher.push(3);
		assertEquals(null, (Object) lastReceived[0]);
		pusher.push(-45);
		assertEquals(null, (Object) lastReceived[0]);
	}

}
