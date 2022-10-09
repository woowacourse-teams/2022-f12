import styled, { css } from 'styled-components';

export const Container = styled.section<{ scrollOffset: number }>`
  position: absolute;
  top: ${({ scrollOffset }) => scrollOffset}px;
  left: 0;

  width: 100vw;
  height: 100vh;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 2;
`;

export const Backdrop = styled.div<{ animationTrigger: boolean }>`
  width: 100%;
  height: 100%;

  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;

  background-color: #00000033;

  height: 100%;
  transition: 200ms;
  ${({ animationTrigger }) => !animationTrigger && 'opacity: 0'};
`;

export const Content = styled.section<{ animationTrigger: boolean }>`
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      width: 20rem;
    }
    @media screen and ${device.tablet} {
      width: 28rem;
    }
    @media screen and ${device.desktop} {
      width: 36rem;
    }
  `}

  min-height: 10rem;
  padding: 1.5rem;
  position: relative;
  z-index: 15;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;

  border-radius: 0.5rem;

  filter: drop-shadow(2px 2px 5px rgba(0, 0, 0, 0.25));
  background-color: ${({ theme }) => theme.colors.white};

  transition: 200ms;
  ${({ animationTrigger }) =>
    !animationTrigger && 'transform: translateY(1rem); opacity: 0'};
`;

export const Title = styled.h1`
  font-size: 1.5rem;
`;

export const Body = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  word-break: break-all;
`;

export const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 2rem;
`;

export const ActionButton = styled.button`
  padding: 0.5rem 1rem;
  border-radius: 0.3rem;
  border: none;

  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.25));

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;

export const ConfirmButton = styled(ActionButton)`
  background-color: ${({ theme }) => theme.colors.primary};
`;

export const CloseButton = styled(ActionButton)`
  background-color: ${({ theme }) => theme.colors.secondary};
`;
