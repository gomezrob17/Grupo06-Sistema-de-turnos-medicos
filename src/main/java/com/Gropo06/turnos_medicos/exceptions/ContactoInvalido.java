package com.Gropo06.turnos_medicos.exceptions;

//Excepción para cuando no se encuentra un Contacto.
public class ContactoInvalido extends RuntimeException {
	public ContactoInvalido(String message) {
		super(message);
	}
}
