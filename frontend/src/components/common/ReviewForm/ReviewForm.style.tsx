import styled from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

export const Title = styled.h1`
  font-size: 1.2rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Label = styled.label`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const LabelTop = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Textarea = styled.textarea`
  resize: none;
  padding: 1rem;
  line-height: 1.5;

  height: 10rem;
`;

export const SubmitButton = styled.button`
  width: max-content;
  padding: 0.5rem 1rem;
  align-self: flex-end;
  background-color: #f6bebe;
  border-radius: 0.3rem;
  border: none;
  cursor: pointer;
`;
