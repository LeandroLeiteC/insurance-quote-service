package br.com.itau.insurance_quote.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN in);
}
