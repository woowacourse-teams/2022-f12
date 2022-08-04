import styled from 'styled-components';

export const Container = styled.div<{
  paddingTopBottom: number;
  paddingLeftRight: number;
  fontSize: number;
}>`
  width: max-content;
  padding: ${({ paddingTopBottom, paddingLeftRight }) =>
    `${paddingTopBottom}rem ${paddingLeftRight}rem`};

  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 2rem;

  font-weight: 500;
  font-size: ${({ fontSize }) => `${fontSize}rem`};
`;
