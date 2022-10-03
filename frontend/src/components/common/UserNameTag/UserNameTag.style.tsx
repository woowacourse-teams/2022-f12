import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 0.4rem;
`;

export const Avatar = styled.img`
  border-radius: 50%;

  object-fit: cover;
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      width: 1.5rem;
      height: 1.5rem;
    }
    @media screen and ${device.desktop} {
      width: 2rem;
      height: 2rem;
    }
  `}
`;

export const Username = styled.p``;
