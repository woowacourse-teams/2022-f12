import styled from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
`;

export const Title = styled.h1`
  font-size: 1.2rem;
`;

export const Wrapper = styled.div<{ columns: number }>`
  display: grid;
  grid-template-columns: ${({ columns }) => `repeat(${columns}, 1fr)`};
  gap: 2rem 1rem;
  justify-items: center;
  padding: 1rem;
`;
