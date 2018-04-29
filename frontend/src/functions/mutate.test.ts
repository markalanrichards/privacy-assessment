import { mutate } from "./mutate";

test("muate", async () => {
  expect(
    await mutate({
      externalEmail: "james@test.com",
      externalLegalName: "james"
    })
  ).toEqual({
    variables: { externalEmail: "james@test.com", externalLegalName: "james" }
  });
});
