import * as React from "react";
import styled from "styled-components";

const Article = styled.article`
  grid-area: null;
`;
interface AtomProps {
  children: JSX.Element | string;
}

export const Wrapper: React.SFC<AtomProps> = ({ children }): JSX.Element => (
  <Article>{children}</Article>
);
