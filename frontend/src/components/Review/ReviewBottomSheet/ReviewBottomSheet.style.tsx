import styled, { css } from 'styled-components';

export const Button = styled.button`
  position: absolute;
  top: 3rem;
  right: 3rem;

  background-color: transparent;
  border-radius: 0.3rem;
  border: none;

  font-size: 1rem;
  font-weight: 500;

  &:hover {
    text-decoration: underline;
  }

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      top: 1.5rem;
      right: 1.5rem;
    }
  `}
`;
