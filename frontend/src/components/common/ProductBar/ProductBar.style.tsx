import styled from 'styled-components';

export const Container = styled.div<{ isSelected: boolean }>`
  width: 30rem;
  height: 3rem;

  display: flex;
  justify-content: center;
  align-items: center;

  font-size: 1.25rem;
  font-weight: 600;

  border-radius: 0.5rem;
  border: solid
    ${({ theme, isSelected }) =>
      isSelected
        ? `3px ${theme.colors.primary}`
        : `2px ${theme.colors.secondary}`};
  background-color: ${({ theme }) => theme.colors.white};
`;
