import styled from 'styled-components';

export const Container = styled.div`
  display: grid;
  grid-template-columns: repeat(6, 1fr);
`;

export const CategoryTitle = styled.h2`
  font-size: 1.2rem;
`;

export const EditDeskSetupButton = styled.button`
  padding: 0.4rem 1.4rem;
  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 0.4rem;
  box-shadow: 4px 4px 10px ${({ theme }) => theme.colors.secondary};
  width: max-content;
`;

export const FlexWrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const PseudoButton = styled.div`
  background-color: transparent;
  border: none;
  cursor: pointer;
`;
