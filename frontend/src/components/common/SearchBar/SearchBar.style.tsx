import styled, { css } from 'styled-components';

export const Container = styled.div`
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      width: 80%;
    }
    @media screen and ${device.tablet} {
      width: 70%;
    }
    @media screen and ${device.desktop} {
      width: 70%;
    }
  `}
  display: flex;
  align-items: center;
`;

export const Input = styled.input`
  width: 100%;
  height: 2.8rem;
  background: #ffffff;
  outline: none;
  border: 1px solid ${({ theme }) => theme.colors.primary};
  border-radius: 1.625rem;
  padding: 0 3.5rem 0 1.5rem;
  font-size: 1rem;

  &:focus {
    border: 2px solid ${({ theme }) => theme.colors.primary};
    transition: 0.3s linear;
  }
`;

export const Button = styled.button`
  width: 3.5rem;
  height: 2.8rem;
  margin-left: -3.5rem;
  background: none;
  border: none;
  outline: none;
`;
