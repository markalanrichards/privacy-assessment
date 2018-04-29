import styled from "styled-components";

const Title = styled.h1`
  color: ${props => props.theme.color.color};
  font-size: ${props => props.theme.size.large};
`;

const Super = Title.extend`
  font-size: ${props => props.theme.size.xxl};
  text-transform: uppercase;
`;
export { Super, Title as Normal };
