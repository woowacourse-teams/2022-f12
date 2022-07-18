import styled, { DefaultTheme } from 'styled-components';

const borderByType = (
  theme: DefaultTheme,
  type: 'default' | 'selected' | 'add'
) => {
  if (type === 'default') return `2px solid ${theme.colors.secondary}`;
  if (type === 'selected') return `3px solid ${theme.colors.primary}`;
  if (type === 'add') return `3px dashed ${theme.colors.primary}`;
};

export const Container = styled.p<{
  barType: 'default' | 'selected' | 'add';
}>`
  width: 36rem;
  height: 3rem;

  display: flex;
  justify-content: center;
  align-items: center;

  border-radius: 0.5rem;
  border: ${({ theme, barType }) => borderByType(theme, barType)};
  background-color: ${({ theme }) => theme.colors.white};
`;

export const Name = styled.p`
  width: 34rem;

  font-size: 1.25rem;
  font-weight: 600;
  text-align: center;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

export const AddContainer = styled(Container)`
  cursor: pointer;
`;
