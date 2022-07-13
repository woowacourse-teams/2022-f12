import styled from 'styled-components';

export const Container = styled.main`
  display: flex;
  justify-content: space-around;
  max-width: 1320px;
  width: 100%;
  margin: auto;
  padding: 50px 0;
  gap: 3rem;
`;

export const Wrapper = styled.div`
  position: relative;

  display: flex;
  flex-direction: column;
  width: 600px;
  height: 100%;
`;

export const FloatingButton = styled.button`
  position: fixed;
  bottom: 50px;
  right: 50px;

  width: 45px;
  height: 45px;

  border-radius: 50%;

  font-size: 2rem;
`;
