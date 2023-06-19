package com.lucassilvs.libteste.service;

import com.lucassilvs.libteste.request.MensagemRequest;
public interface MensagemService {
     void postarMensagem (MensagemRequest mensagem);

     void postarMensagem (MensagemRequest mensagem, String nomeTopico);
}
