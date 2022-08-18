declare type UserData = {
  member: Omit<Member, 'careerLevel' | 'jobType'>;
  token: string;
  registerCompleted: boolean;
};
