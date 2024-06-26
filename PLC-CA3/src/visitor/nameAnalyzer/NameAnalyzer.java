package visitor.nameAnalyzer;

import ast.node.Program;
import ast.node.declaration.*;
import ast.node.statement.*;
import compileError.*;
import compileError.Name.*;
import symbolTable.SymbolTable;
import symbolTable.symbolTableItems.*;
import symbolTable.itemException.ItemAlreadyExistsException;
import symbolTable.symbolTableItems.VariableItem;
import visitor.Visitor;

import java.util.ArrayList;

public class NameAnalyzer extends Visitor<Void> {


    public ArrayList<CompileError> nameErrors = new ArrayList<>();
    private static int i = 0;

    @Override
    public Void visit(Program program) {
        SymbolTable.root = new SymbolTable();
        SymbolTable.push(SymbolTable.root);

        for (FuncDeclaration functionDeclaration : program.getFuncs()) {
            functionDeclaration.accept(this);
        }
//        program.getMain().accept(this);

        for (var stmt : program.getMain().getMainStatements()) {
            if(stmt instanceof VarDecStmt) {
                stmt.accept(this);
            }
            if(stmt instanceof ArrayDecStmt) {
                stmt.accept(this);
            }
            if(stmt instanceof ForloopStmt) {
                stmt.accept(this);
            }
            if(stmt instanceof ImplicationStmt) {
                stmt.accept(this);
            }
        }

        return null;
    }

//    @Override
//    public Void visit(MainDeclaration mainDeclaration)
//    {
//        var mainItem = new MainItem(mainDeclaration);
//        var mainSymbolTable = new SymbolTable(SymbolTable.top, "main");
//        mainItem.setMainItemSymbolTable(mainSymbolTable);
//        SymbolTable.push(mainItem.getMainItemSymbolTable());
//        for (var stmt : mainDeclaration.getMainStatements())
//        {
//            if(stmt instanceof VarDecStmt) {
//                stmt.accept(this);
//            }
//            if(stmt instanceof ArrayDecStmt) {
//                stmt.accept(this);
//            }
//            if(stmt instanceof ForloopStmt) {
//                stmt.accept(this);
//            }
//            if(stmt instanceof ImplicationStmt) {
//                stmt.accept(this);
//            }
//        }
//        return null;
//    }



    @Override
    public Void visit(FuncDeclaration funcDeclaration) {
        var functionItem = new FunctionItem(funcDeclaration);
        var functionSymbolTable = new SymbolTable(SymbolTable.top, funcDeclaration.getName().getName());
        functionItem.setFunctionSymbolTable(functionSymbolTable);

        boolean done = false;
        while(!done) {
            try {
                SymbolTable.top.put(functionItem);
                done = true;
            } catch (ItemAlreadyExistsException e) {
                var error = new FunctionRedefinition(funcDeclaration.getLine(), funcDeclaration.getName().getName());
                nameErrors.add(error);
                functionItem.setName(funcDeclaration.getName().getName() + "$%:)" + this.i);
            }
            this.i++;
        }

        SymbolTable.push(functionSymbolTable);
        for (ArgDeclaration argDeclaration : funcDeclaration.getArgs()) {
            argDeclaration.accept(this);
        }

        for (var stmt : funcDeclaration.getStatements()) {
            if(stmt instanceof VarDecStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ArrayDecStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ForloopStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ImplicationStmt) {
                stmt.accept(this);
            }
        }

        SymbolTable.pop();
        return null;
    }

    @Override
    public Void visit(ForloopStmt forloopStmt) {

//        ForLoopItem forLoopItem = new ForLoopItem(forloopStmt);
//        var forLoopSymbolTable = new SymbolTable(SymbolTable.top, forloopStmt.toString());
//        forLoopItem.setForLoopSymbolTable(forLoopSymbolTable);
//        try {
//            SymbolTable.top.put(forLoopItem);
//        }
//        catch (ItemAlreadyExistsException e) {
//
//        }
        SymbolTable forLoopSymbolTable = new SymbolTable(SymbolTable.top, forloopStmt.toString());
        SymbolTable.push(forLoopSymbolTable);

        for(Statement stmt: forloopStmt.getStatements()) {
            if(stmt instanceof VarDecStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ArrayDecStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ForloopStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ImplicationStmt) {
                stmt.accept(this);
            }
        }

        SymbolTable.pop();
        return null;
    }

    @Override
    public Void visit(ImplicationStmt implicationStmt) {
//        ImplicationItem implicationItem = new ImplicationItem(implicationStmt);
//        var impSymbolTable = new SymbolTable(SymbolTable.top, implicationStmt.toString());
//        implicationItem.setImplicationSymbolTable(impSymbolTable);
//        try
//        {
//            SymbolTable.top.put(implicationItem);
//        }
//        catch (ItemAlreadyExistsException e)
//        {
//        }
        SymbolTable impSymbolTable = new SymbolTable(SymbolTable.top, implicationStmt.toString());
        SymbolTable.push(impSymbolTable);

        for(Statement stmt: implicationStmt.getStatements()) {
            if(stmt instanceof VarDecStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ArrayDecStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ForloopStmt) {
                stmt.accept(this);
            }

            if(stmt instanceof ImplicationStmt) {
                stmt.accept(this);
            }
        }
        SymbolTable.pop();
        return null;
    }

    @Override
    public Void visit(VarDecStmt varDeclaration) {
        VariableItem variableItem = new VariableItem(varDeclaration.getIdentifier().getName(), varDeclaration.getType());
        try {
            SymbolTable.top.put(variableItem);
        } catch (ItemAlreadyExistsException e) {
            var error = new VariableRedefinition(varDeclaration.getLine(), varDeclaration.getIdentifier().getName());
            nameErrors.add(error);
        }

        return null;
    }

    @Override
    public Void visit(ArrayDecStmt arrayDecStmt) {
        var arrayItem = new ArrayItem(arrayDecStmt.getIdentifier().getName(), arrayDecStmt.getType());
        try {
            SymbolTable.top.put(arrayItem);
        } catch (ItemAlreadyExistsException e) {
            var error = new VariableRedefinition(arrayDecStmt.getLine(), arrayDecStmt.getIdentifier().getName());
            nameErrors.add(error);
        }


        return null;
    }

    @Override
    public Void visit(ArgDeclaration argDecStmt) {
        VariableItem variableItem = new VariableItem(argDecStmt.getIdentifier().getName(), argDecStmt.getType());
        try {
            SymbolTable.top.put(variableItem);
        } catch (ItemAlreadyExistsException e) {
            var error = new VariableRedefinition(argDecStmt.getLine(), argDecStmt.getIdentifier().getName());
            nameErrors.add(error);
        }

        return null;
    }
}
