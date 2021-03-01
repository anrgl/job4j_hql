package ru.job4j.hql.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "jobs_base")
public class JobBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    public static JobBase of(String name) {
        JobBase base = new JobBase();
        base.setName(name);
        return base;
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobBase jobBase = (JobBase) o;
        return id == jobBase.id &&
                Objects.equals(jobs, jobBase.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobs);
    }

    @Override
    public String toString() {
        return "JobBase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobs=" + jobs +
                '}';
    }
}
