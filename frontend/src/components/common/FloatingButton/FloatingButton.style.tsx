import styled from 'styled-components';

export const Button = styled.button`
  width: 3rem;
  height: 3rem;

  display: flex;
  justify-content: center;
  align-items: center;

  position: fixed;
  bottom: 50px;
  right: 50px;

  color: ${({ theme }) => theme.colors.white};
  font-size: 2rem;
  font-weight: 600;

  border-radius: 50%;
  border: none;

  background-color: ${({ theme }) => theme.colors.primary};

  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.25));

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;
