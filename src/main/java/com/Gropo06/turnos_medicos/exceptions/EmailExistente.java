package com.Gropo06.turnos_medicos.exceptions;

public class EmailExistente extends RuntimeException{

		public EmailExistente(String mensaje) {
			super(mensaje);
		}
		
}
