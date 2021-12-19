package pias.backend.id.server.jaxrs.model;

public record PIAAnnex2JaxRs(
    String step1,
    String step2Part1,
    String step2Part2,
    PIAAnnex2Step3JaxRs step3,
    PIAAnnex2Step4JaxRs step4,
    PIAAnnex2Step5JaxRs step5,
    PIAAnnex2Step6JaxRs step6) {}
