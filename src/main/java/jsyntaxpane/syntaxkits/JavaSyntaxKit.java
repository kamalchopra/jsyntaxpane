/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License 
 *       at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 */
package jsyntaxpane.syntaxkits;

import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.text.Keymap;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.Lexer;
import jsyntaxpane.actions.LineNumbersRule;
import jsyntaxpane.TokenType;
import jsyntaxpane.actions.FindReplaceActions;
import jsyntaxpane.actions.Markers;
import jsyntaxpane.actions.MapCompletion;
import jsyntaxpane.actions.PairsMarker;
import jsyntaxpane.actions.SyntaxActions;
import jsyntaxpane.actions.TokenMarker;
import jsyntaxpane.lexers.JavaLexer;
import jsyntaxpane.util.JarServiceProvider;

/**
 *
 * @author Ayman Al-Sairafi
 */
public class JavaSyntaxKit extends DefaultSyntaxKit {

    public JavaSyntaxKit() {
        super(new JavaLexer());
    }

    /**
     * Consruct a JavaSyntaxKit user the supplied lexer.  This is protected so
     * only subclasses may extend this with a new lexer.
     * @param lexer
     */
    JavaSyntaxKit(Lexer lexer) {
        super(lexer);
    }

    @Override
    public void addKeyActions(Keymap map) {
        super.addKeyActions(map);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), SyntaxActions.JAVA_INDENT);
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control SPACE"),
                new MapCompletion(getCompletions()));
        FindReplaceActions finder = new FindReplaceActions();
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control F"), finder.getFindDialogAction());
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("control H"), finder.getReplaceDialogAction());
        map.addActionForKeyStroke(KeyStroke.getKeyStroke("F3"), finder.getFindNextAction());
    }

    @Override
    public void install(JEditorPane editorPane) {
        super.install(editorPane);
        tokenMarker = new TokenMarker(editorPane, new Color(0xffffbb),
                getHighlightTokenTypes());
        pairMarker = new PairsMarker(editorPane, Color.ORANGE);

        editorPane.addCaretListener(tokenMarker);
        editorPane.addCaretListener(pairMarker);
        lineNumbers = new LineNumbersRule(editorPane);
    }

    @Override
    public void deinstall(JEditorPane editorPane) {
        super.deinstall(editorPane);
        editorPane.removeCaretListener(pairMarker);
        editorPane.removeCaretListener(tokenMarker);
        Markers.removeMarkers(editorPane);
        editorPane.removeCaretListener(lineNumbers);
    }
    
    private TokenMarker tokenMarker;
    private PairsMarker pairMarker;
    private LineNumbersRule lineNumbers;
    private static Map<String, String> COMPLETIONS;
    private static Set<TokenType> HIGHLITED_TOKENTYPES = new HashSet<TokenType>();

    /**
     * returns the completions Map
     * @return
     */
    public static Map<String, String> getCompletions() {
        return COMPLETIONS;
    }

    /**
     * Returns the TokenTypes to highlight
     * @return
     */
    public static Set<TokenType> getHighlightTokenTypes() {
        return HIGHLITED_TOKENTYPES;
    }


    static {
        COMPLETIONS = JarServiceProvider.readStringsMap("jsyntaxpane.javasyntaxkit.completions");
        HIGHLITED_TOKENTYPES.add(TokenType.IDENTIFIER);
        HIGHLITED_TOKENTYPES.add(TokenType.TYPE);
        HIGHLITED_TOKENTYPES.add(TokenType.TYPE2);
        HIGHLITED_TOKENTYPES.add(TokenType.TYPE3);
    }
}
