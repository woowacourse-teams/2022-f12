import styled from 'styled-components';

export const Container = styled.div`
  height: 100%;

  position: sticky;
  top: ${({ theme }) => theme.headerHeight};
  left: 0;
`;
