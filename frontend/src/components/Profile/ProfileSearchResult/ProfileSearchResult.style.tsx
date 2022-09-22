import styled from 'styled-components';

export const Container = styled.div`
  max-width: 80%;

  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 3rem 12rem;
  justify-items: center;
  margin: 0 auto;
`;

export const CardWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

export const NoDataContainer = styled.div`
  display: flex;
  justify-content: center;
`;
