package com.uditagarwal.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pavan.OutputPrinter;
import com.pavan.commands.ColorToRegNumberCommandExecutor;
import com.pavan.model.Car;
import com.pavan.model.Command;
import com.pavan.model.Slot;
import com.pavan.service.ParkingLotService;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

public class ColorToRegNumberCommandExecutorTest {
  private ParkingLotService parkingLotService;
  private OutputPrinter outputPrinter;
  private ColorToRegNumberCommandExecutor colorToRegNumberCommandExecutor;

  @Before
  public void setUp() throws Exception {
    parkingLotService = mock(ParkingLotService.class);
    outputPrinter = mock(OutputPrinter.class);
    colorToRegNumberCommandExecutor =
        new ColorToRegNumberCommandExecutor(parkingLotService, outputPrinter);
  }

  @Test
  public void testValidCommand() {
    assertTrue(
        colorToRegNumberCommandExecutor.validate(
            new Command("registration_numbers_for_cars_with_colour white")));
  }

  @Test
  public void testInvalidCommand() {
    assertFalse(
        colorToRegNumberCommandExecutor.validate(
            new Command("registration_numbers_for_cars_with_colour")));
    assertFalse(
        colorToRegNumberCommandExecutor.validate(
            new Command("registration_numbers_for_cars_with_colour a b")));
  }

  @Test
  public void testWhenNoCarsFoundWithAColor() {
    when(parkingLotService.getSlotsForColor("white")).thenReturn(Collections.emptyList());
    colorToRegNumberCommandExecutor.execute(
        new Command("registration_numbers_for_cars_with_colour white"));

    verify(outputPrinter).notFound();
  }

  @Test
  public void testCarsWithAColor() {
    final Slot slot1 = new Slot(1);
    slot1.assignCar(new Car("num_white1", "white"));
    final Slot slot2 = new Slot(2);
    slot2.assignCar(new Car("num_white2", "white"));
    when(parkingLotService.getSlotsForColor("white"))
        .thenReturn(Arrays.asList(slot1, slot2));
    colorToRegNumberCommandExecutor.execute(
        new Command("registration_numbers_for_cars_with_colour white"));

    verify(outputPrinter).printWithNewLine("num_white1, num_white2");
  }
}
