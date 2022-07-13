import styled from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: flex-end;

  position: absolute;
  bottom: 0;
  left: 0;

  width: 100%;
  height: 100%;
`;

export const Backdrop = styled.div`
  position: relative;
  z-index: 1;

  height: 100%;
`;

export const Content = styled.section`
  width: 100%;
  height: max-content;
  min-height: 200px;

  position: relative;
  z-index: 2;

  padding: 1rem;

  border-top-left-radius: 1rem;
  border-top-right-radius: 1rem;

  filter: drop-shadow(0px -10px 30px rgba(0, 0, 0, 0.25));
  background-color: ${({ theme }) => theme.colors.white};
`;