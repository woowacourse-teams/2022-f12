import styled from 'styled-components';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Selected = styled.div`
  width: max-content;
`;

export const OptionsList = styled.ul`
  width: max-content;
  display: flex;
  flex-direction: column;
`;

export const Option = styled.li`
  width: max-content;
`;

export const PseudoButton = styled.button`
  display: flex;
  align-items: center;
  gap: 0.5rem;

  background-color: transparent;
  border: none;
`;

export const EditButton = styled.button`
  align-self: flex-end;
  width: max-content;
  padding: 0.5rem 1rem;
  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 0.3rem;
  border: none;

  filter: drop-shadow(1px 1px 2px rgba(0, 0, 0, 0.25));

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;

export const NoContentMessage = styled.p`
  text-align: center;
`;
