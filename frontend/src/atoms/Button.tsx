import * as React from "react";
import styled from "../functions/styled";

export interface AtomProps {
  url: string;
  title: string;
}

const DefaultButton = styled.button`
  color: ${props => props.theme.color.primaryColor};
  font-size: ${props => props.theme.size.large};
  border: none;
`;

export const Button: React.SFC<AtomProps> = ({ url, title }) => {
  return (
    <a href={url}>
      <DefaultButton type="button">{title}</DefaultButton>
    </a>
  );
};
