package ru.job4j.hql.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int experience;

    private double salary;

    @OneToOne(fetch = FetchType.LAZY)
    private JobBase base;

    public static Candidate of(String name, int exp, double salary, JobBase base) {
        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidate.setExperience(exp);
        candidate.setSalary(salary);
        candidate.setBase(base);
        return candidate;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public JobBase getBase() {
        return base;
    }

    public void setBase(JobBase base) {
        this.base = base;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return id == candidate.id &&
                experience == candidate.experience &&
                Double.compare(candidate.salary, salary) == 0 &&
                Objects.equals(name, candidate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, experience, salary);
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", experience=" + experience +
                ", salary=" + salary +
                ", base=" + base +
                '}';
    }
}
