import styled from 'styled-components';

export const Container = styled.div<{ triggerAnimation: boolean }>`
  position: absolute;
  bottom: -${({ theme }) => theme.headerHeight};

  width: 100%;
  height: ${({ theme }) => theme.headerHeight};

  background-color: ${({ theme }) => theme.colors.white};
  z-index: 3;

  transition: 300ms ease;

  ${({ triggerAnimation }) =>
    !triggerAnimation && 'transform: translateY(-1rem); z-index: -1; opacity: 0'};

  &::after {
    position: absolute;
    left: 0;
    top: 3rem;
    content: '';
    height: 0.3rem;
    width: 100%;
    background: linear-gradient(
      180deg,
      rgba(60, 60, 60, 0.1) 0%,
      rgba(60, 60, 60, 0) 100%
    );
  }
`;

export const Wrapper = styled.div`
  max-width: 1320px;
  width: 100%;
  height: 100%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 1rem;
`;
