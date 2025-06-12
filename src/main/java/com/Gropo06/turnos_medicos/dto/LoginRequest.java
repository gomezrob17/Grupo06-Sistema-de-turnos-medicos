package com.Gropo06.turnos_medicos.dto;

public record LoginRequest(
  String email,
  String password
) { }