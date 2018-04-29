import { freshCleanState } from "./CreatePIA";

test("sfd", () => {
  const result = Object.keys(freshCleanState.document.annex1).map(
    question => question
  );

  // expect(result).toBe("j");
});
