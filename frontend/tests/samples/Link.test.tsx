import * as React from "react";
import * as renderer from "react-test-renderer";
import { Link } from "./Link";

describe("Jest react tests", () => {
  test("Jest - Link changes the class when hovered", () => {
    const component = renderer.create(
      <Link href="http://www.facebook.com">Facebook</Link>
    );
    let tree = component.toJSON();
    expect(tree).toMatchSnapshot();

    // manually trigger the callback
    tree.props.onMouseEnter();
    // re-rendering
    tree = component.toJSON();
    expect(tree).toMatchSnapshot();

    // manually trigger the callback
    tree.props.onMouseLeave();
    // re-rendering
    tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });
});
