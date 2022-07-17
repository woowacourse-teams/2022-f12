import styled from 'styled-components';

export const Container = styled.div`
  width: max-content;
  padding: 0.5rem 1.8rem;

  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 2rem;

  font-weight: 500;
`;
