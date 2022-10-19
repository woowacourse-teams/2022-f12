import styled, { css } from 'styled-components';

export const Container = styled.section`
  position: fixed;
  bottom: 120px;
  right: 30px;
  z-index: 1;
`;

export const Button = styled.button`
  display: flex;
  width: max-width;
  padding: 0 1rem;
  height: 3rem;

  justify-content: center;
  align-items: center;

  ${({ theme: { device } }) => css`
    @media screen and ${device.desktop} {
      right: 50px;
    }
  `}

  color: ${({ theme }) => theme.colors.white};
  font-size: 2rem;
  font-weight: 600;

  border-radius: 50px;
  border: none;

  background-color: ${({ theme }) => theme.colors.primary};

  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.25));

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;
