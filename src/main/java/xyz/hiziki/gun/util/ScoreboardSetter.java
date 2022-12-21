package xyz.hiziki.gun.util;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;


public class ScoreboardSetter
{
    enum TeamKindEnum
    {
        Red("team_red","赤",ChatColor.RED),
        Blue("team_blue","青",ChatColor.BLUE);

        private final String TeamName;
        private final String ViewTeamName;
        private final ChatColor TeamColor;

        TeamKindEnum(String _TeamName,String _ViewTeamName, ChatColor _TeamColor)
        {
            this.TeamName = _TeamName;
            this.ViewTeamName = _ViewTeamName;
            this.TeamColor = _TeamColor;
        }

        public String getTeamName()
        {
            return TeamName;
        }

        public String getViewTeamName()
        {
            return ViewTeamName + "チーム";
        }

        public ChatColor getTeamColor()
        {
            return TeamColor;
        }

        public String getPrefix()
        {
            return "【"+this.getViewTeamName()+"】";
        }

        public static TeamKindEnum GetEnum(String TeamName)
        {
            for (TeamKindEnum team: TeamKindEnum.values() )
            {
                if(team.getTeamName().equals(TeamName)) return team;
            }
            return null;
        }
    }

    public List<Team> teams;

    public List<Score> scoreList;

    public Scoreboard ScoreBord;
    private List<String> tmpPlayerNameList;

    public ScoreboardSetter(Server server)
    {
        ScoreboardManager manager = server.getScoreboardManager();
        ScoreBord = manager.getMainScoreboard();
        ScoreBord.clearSlot(DisplaySlot.SIDEBAR);
        teams = new ArrayList<>();

        for (Team team : ScoreBord.getTeams())
        {
            team.unregister();
        }

        // チームが既に登録されているかどうか確認し、
        // 登録されていないなら新規作成します。

        for (TeamKindEnum team : TeamKindEnum.values())
        {
            if(ScoreBord.getTeam(team.getTeamName()) == null)
            {
                Team tmpTeam = ScoreBord.registerNewTeam(team.getTeamName());
                tmpTeam.setColor(team.getTeamColor());
                tmpTeam.setPrefix(team.getViewTeamName());
                tmpTeam.setDisplayName(team.getPrefix());
                tmpTeam.setAllowFriendlyFire(false);
                teams.add(tmpTeam);
            }
        }

        tmpPlayerNameList = new ArrayList<>();

        for (Player player : server.getOnlinePlayers())
        {
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            tmpPlayerNameList.add(player.getName());
        }

        FirstSetScoreView();
        SetTeamMember();
    }

    public void SetTeamMember()
    {
        for (Team team: teams)
        {
            for (String tmp : team.getEntries())
            {
                team.removeEntry(tmp);
            }
        }
        while (!tmpPlayerNameList.isEmpty())
        {
            for (Team team: teams)
            {
                int No = (int) (Math.random() * ((tmpPlayerNameList.size() - 1)));
                team.addEntry(tmpPlayerNameList.get(No));
                tmpPlayerNameList.remove(No);
                if (tmpPlayerNameList.isEmpty()) break;
            }
        }
    }

    public void FirstSetScoreView()
    {
        Objective objective = ScoreBord.getObjective("GunScore");

        if (objective == null)
        {
            objective = ScoreBord.registerNewObjective("GunScore", "Kill", "スコア状況");
        }

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("キル状況");

        scoreList = new ArrayList<>();

        for (TeamKindEnum team: TeamKindEnum.values())
        {
            Score tmpScore = objective.getScore(team.getTeamColor() + team.getViewTeamName()+"：");
            tmpScore.setScore(0);
            scoreList.add(tmpScore);
        }
    }

    public void PointCheck(Player Killer, Player target)
    {
        for (Team team: teams)
        {
            if (team.hasEntry(Killer.getName()))
            {
                if (team.hasEntry(target.getName()))
                {
                    TeamPoint(TeamKindEnum.GetEnum(team.getName()), -1);
                }
                else
                {
                    TeamPoint(TeamKindEnum.GetEnum(team.getName()), 1);
                }
            }
        }
    }

    public void TeamPoint(TeamKindEnum TeamKind, int count)
    {
        int tmp = scoreList.get(TeamKind.ordinal()).getScore();
        tmp = tmp + count;
        scoreList.get(TeamKind.ordinal()).setScore(tmp);
    }
}
