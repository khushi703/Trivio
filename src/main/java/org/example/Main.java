package org.example;

import org.example.entities.Participant;
import org.example.entities.Question;
import org.example.entities.Quiz;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Build the SessionFactory using Hibernate configuration file
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory()) {

            // Open a new session
            try (Session session = sessionFactory.openSession()) {

                // Begin transaction
                Transaction transaction = session.beginTransaction();

                try {
                    // Create a new Quiz
                    Quiz quiz = new Quiz();
                    quiz.setTitle("GDSC Quiz");
                    quiz.setNumQuestions(5);
                    quiz.setCreatedAt(LocalDateTime.now());

                    // Add Questions to the Quiz
                    Question question1 = new Question();
                    question1.setQuestionText("What is Java?");
                    question1.setOptions("A. Programming Language, B. Framework, C. Database, D. None");
                    question1.setCorrectAnswer("A");
                    question1.setTimer(30);
                    question1.setQuiz(quiz); // Set parent

                    Question question2 = new Question();
                    question2.setQuestionText("What is Hibernate?");
                    question2.setOptions("A. ORM, B. Database, C. IDE, D. None");
                    question2.setCorrectAnswer("A");
                    question2.setTimer(30);
                    question2.setQuiz(quiz); // Set parent

                    // Add questions to the quiz
                    List<Question> questions = new ArrayList<>();
                    questions.add(question1);
                    questions.add(question2);
                    quiz.setQuestions(questions);

                    // Create Participants
                    Participant participant1 = new Participant();
                    participant1.setName("Alice");

                    Participant participant2 = new Participant();
                    participant2.setName("Bob");

                    // Add participants to the quiz
                    List<Participant> participants = new ArrayList<>();
                    participants.add(participant1);
                    participants.add(participant2);
                    quiz.setParticipants(participants);

                    // Save Quiz (Cascades save operations to Questions and Participants)
                    session.persist(quiz);

                    // Commit the transaction
                    transaction.commit();

                    System.out.println("Entities successfully saved!");

                } catch (Exception e) {
                    // Rollback the transaction in case of an error
                    if (transaction != null) transaction.rollback();
                    System.err.println("Transaction rolled back due to an error:");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // Handle SessionFactory initialization errors
            System.err.println("SessionFactory initialization failed:");
            e.printStackTrace();
        }
    }
}
