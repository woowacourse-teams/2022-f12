import styled from 'styled-components';

export const Button = styled.button<{
  fontSize: number;
  clicked: boolean;
}>`
  width: max-content;
  font-size: ${({ fontSize }) => `${fontSize}px`};
  padding: 0.2rem 0.5rem;

  background-color: ${({ theme, clicked }) =>
    clicked ? theme.colors.primary : theme.colors.secondary};
  border-radius: 2rem;
  border: none;
  font-weight: 500;

  &:hover {
    background-color: ${({ theme }) => theme.colors.primary};
  }
`;
