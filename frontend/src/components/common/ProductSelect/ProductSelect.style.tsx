import styled from 'styled-components';

export const Container = styled.div``;

export const Selected = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
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
  background-color: transparent;
  border: none;
`;
