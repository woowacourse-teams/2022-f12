import styled from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: flex-end;

  position: fixed;
  bottom: 0;
  right: 3rem;

  width: 50%;

  transform-origin: bottom;
  animation: slide-up 0.2s ease;

  @keyframes slide-up {
    0% {
      transform: translateY(20rem);
      opacity: 0;
    }
    100% {
      transform: translateY(0rem);
      opacity: 1;
    }
  }
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
`;
