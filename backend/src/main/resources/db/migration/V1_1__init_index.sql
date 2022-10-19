alter table product add index idx_rc_ar_id(review_count desc, avg_rating desc, id desc);
alter table product add index idx_ar_rc_id(avg_rating desc, review_count desc, id desc);
alter table product add index idx_category(category);

alter table member add index idx_cl_jt(career_level, job_type);
alter table member add index idx_jt(job_type);
alter table member add index idx_reg_fc_id(registered, follower_count desc, id desc);
