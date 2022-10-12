import styled, { css } from 'styled-components';

export const Container = styled.section<{
  animationTrigger?: boolean;
}>`
  display: flex;
  flex-direction: column;
  justify-content: flex-end;

  position: fixed;

  width: 90%;

  right: 5%;
  bottom: 0;

  transition: 200ms;

  ${({ animationTrigger }) =>
    !animationTrigger && 'transform: translateY(20rem); opacity: 0;'}

  ${({ theme: { device } }) => css`
    @media screen and ${device.desktop} {
      right: 3rem;

      width: 50%;
    }
  `}
`;

export const Backdrop = styled.div`
  position: relative;
  z-index: 1;

  height: 100%;
`;

export const Content = styled.section`
  width: 100%;
  padding: 3rem;

  position: relative;
  z-index: 2;

  filter: drop-shadow(2px 2px 5px rgba(0, 0, 0, 0.25));
  background-color: ${({ theme }) => theme.colors.white};

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      padding: 1.5rem;
    }
  `}
`;
